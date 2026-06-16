import type {
	FrontendContribution,
	FrontendHostApi,
} from "@mareano-frontend/extensions/frontendExtensionsRegistry";

interface FeatureHitInput {
	layerId: string;
	layerName?: string;
	feature: {
		properties?: Record<string, unknown>;
	};
}

interface FylkerInfo {
  fylkesnummer: number;
  navn_pri_1: string;
  navn_pri_2?: string;
  samiskforvaltningsomrade: boolean;
}

function parseFeatureHitInput(input: unknown): FylkerInfo | undefined {
	if (!input || typeof input !== "object") {
		return undefined;
	}

	const candidate = input as Partial<FeatureHitInput>;
	if (!candidate.feature || typeof candidate.feature !== "object" || !candidate.feature.properties) {
		return undefined;
	}

	return {
		fylkesnummer: parseInt(getPropString(candidate.feature.properties, "fylkesnummer") ?? "0", 10),
		navn_pri_1: getPropString(candidate.feature.properties, "navn_pri_1") ?? "?",
		navn_pri_2: getPropString(candidate.feature.properties, "navn_pri_2"),
		samiskforvaltningsomrade: getPropString(candidate.feature.properties, "samiskforvaltningsomrade") === "true",
	};
}

function getLayerName(input: unknown): string | undefined {
	if (!input || typeof input !== "object") {
		return undefined;
	}

	const candidate = input as Partial<FeatureHitInput>;
	return typeof candidate.layerName === "string" ? candidate.layerName : undefined;
}

function getPropString(properties: Record<string, unknown>, key: string): string | undefined {
	const value = properties[key];
	return typeof value === "string" && value.trim().length > 0 ? value.trim() : undefined;
}

function isFylkerLayer(input: unknown): boolean {
  return getLayerName(input) === "Fylker";
}

export function createFeatureHitsFylkerTitleContribution({
  React
}: FrontendHostApi): FrontendContribution {
	return {
		id: "feature-hits-fylker-title",
		purpose: "feature-hit-title",
		shouldRender: isFylkerLayer,
		factory: () => {
			return function FeatureHitsFylkerTitle({ input }) {
				const parsed = parseFeatureHitInput(input);
				return parsed && (
					<>
						{parsed.navn_pri_1}
						{parsed.navn_pri_2 ? ` (${parsed.navn_pri_2})` : null}
					</>
				);
			};
		},
	};
}

export function createFeatureHitsFylkerDetailsContribution({
  React
}: FrontendHostApi): FrontendContribution {
	return {
		id: "feature-hits-fylker-details",
		purpose: "feature-hit-details",
		shouldRender: isFylkerLayer,
		factory: () => {
			return function FeatureHitsFylkerDetails({ input }) {
				const parsed = parseFeatureHitInput(input);
				if (!parsed) {
					return null;
				}
				return (
					<>
						Fylke #{parsed.fylkesnummer}, er {parsed.samiskforvaltningsomrade ? "" : "ikke "}samisk forvaltningsområde. <br/>
					</>
				);
			};
		},
	};
}