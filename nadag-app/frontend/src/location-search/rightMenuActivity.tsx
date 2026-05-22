import type {
	FrontendContribution,
	FrontendHostApi,
} from "@mareano-frontend/extensions/frontendExtensionsRegistry";
import { dispatchMapNavigateEvent } from "@mareano-frontend/extensions/mapNavigateEvent";
import {
	LOCATION_SEARCH_ACTIVITY_ID,
	type GeoJsonFeature,
	type GeoJsonFeatureCollection,
} from "./geojson";

function getFeatureTitle(feature: GeoJsonFeature, index: number): string {
	const properties = feature.properties;
	if (!properties || typeof properties !== "object") {
		return `Feature ${index + 1}`;
	}

	const namedProps = properties as Record<string, unknown>;
	const titleCandidates = [namedProps.title, namedProps.name];

	for (const candidate of titleCandidates) {
		if (typeof candidate === "string" && candidate.trim()) {
			return candidate;
		}
	}

	return `Feature ${index + 1}`;
}

export function createLocationSearchRightMenuActivityContribution({
	React,
}: FrontendHostApi): FrontendContribution {
	return {
		id: LOCATION_SEARCH_ACTIVITY_ID,
		purpose: "right-menu-activity",
		meta: {
			name: "Search",
		},
		factory: () => {
			return function LocationSearchActivity() {
				const [query, setQuery] = React.useState("");
				const [isLoading, setIsLoading] = React.useState(false);
				const [isSelecting, setIsSelecting] = React.useState(false);
				const [error, setError] = React.useState<string | null>(null);
				const [results, setResults] = React.useState<GeoJsonFeatureCollection[]>([]);

				const onSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
					event.preventDefault();
					const trimmed = query.trim();
					if (!trimmed) {
						setResults([]);
						setError("Enter a search term.");
						return;
					}

					setIsLoading(true);
					setError(null);

					try {
						const params = new URLSearchParams({
							q: trimmed,
							text: trimmed,
						});
						const response = await fetch(`/api/location-search?${params.toString()}`, {
							headers: {
								Accept: "application/json",
							},
						});

						if (!response.ok) {
							const message = await response.text();
							throw new Error(message || `Search failed (${response.status})`);
						}

						const data = (await response.json()) as GeoJsonFeatureCollection[];
						setResults(data);
					} catch (caught) {
						const message =
							caught instanceof Error ? caught.message : "Search request failed.";
						setError(message);
						setResults([]);
					} finally {
						setIsLoading(false);
					}
				};

				const onSelectFeature = async (feature: GeoJsonFeature) => {
					setIsSelecting(true);
					setError(null);

					try {
						if (!feature.geometry) {
							throw new Error("Selected feature has no geometry.");
						}

						dispatchMapNavigateEvent({
							geoJson: feature,
							zoom: { max: 15 },
							durationMs: 300,
							padding: [48, 48, 48, 48],
						});
					} catch (caught) {
						const message =
							caught instanceof Error
								? caught.message
								: "Failed to zoom to selected feature.";
						setError(message);
					} finally {
						setIsSelecting(false);
					}
				};

				return (
					<div className="flex flex-col gap-4">
						<form onSubmit={onSubmit} className="flex flex-col gap-2">
							<label htmlFor="location-search-input">Search</label>
							<input
								id="location-search-input"
								type="text"
								value={query}
								placeholder="Search term"
								onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
									setQuery(event.target.value)
								}
								className="rounded border border-neutral-300 bg-white px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-brand-primary"
							/>
							<button
								type="submit"
								disabled={isLoading || isSelecting}
								className="rounded bg-brand-primary px-3 py-2 text-sm font-medium text-white disabled:cursor-not-allowed disabled:opacity-60"
							>
								{isLoading ? "Searching..." : "Search"}
							</button>
						</form>

						{isSelecting ? (
							<p className="text-sm text-neutral-700">Zooming to selected result...</p>
						) : null}

						{error ? (
							<p className="text-sm text-red-700" role="alert">
								{error}
							</p>
						) : null}

						<div className="flex flex-col gap-2">
							{results.length === 0 && !isLoading && !error ? (
								<p className="text-sm text-neutral-700">No results yet.</p>
							) : null}

							{results.map((featureCollection, index) => (
								<section
									key={`${featureCollection.name ?? "collection"}-${index}`}
									className="flex flex-col gap-2 rounded border border-neutral-200 bg-white p-3"
								>
									<p className="text-sm font-semibold text-neutral-900">
										{`${featureCollection.name || `Feature collection ${index + 1}`} (${featureCollection.features?.length ?? 0})`}
									</p>
									<div className="flex flex-col gap-2">
										{(featureCollection.features || []).map((feature, featureIndex) => (
											<button
												key={`${featureCollection.name ?? index}-${featureIndex}`}
												type="button"
												onClick={() => void onSelectFeature(feature)}
												disabled={isSelecting || !feature.geometry}
												className="w-full rounded border border-neutral-200 bg-neutral-50 p-2 text-left text-xs disabled:cursor-not-allowed disabled:opacity-60"
											>
												{getFeatureTitle(feature, featureIndex)}
											</button>
										))}
									</div>
								</section>
							))}
						</div>
					</div>
				);
			};
		},
	};
}