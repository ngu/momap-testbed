import type {
	FrontendContribution,
	FrontendHostApi,
} from "@mareano-frontend/extensions/frontendExtensionsRegistry";

interface FeatureHitInput {
	layerId: string;
	layerName?: string;
	features: {
		properties?: Record<string, unknown>;
	}[];
}

interface KommunerInfo {
  kommunenummer: number;
  navn_pri_1: string;
  navn_pri_2?: string;
}

function parseFeatureHitInput(input: unknown): KommunerInfo | undefined {
	if (!input || typeof input !== "object") {
		return undefined;
	}
  
	const candidate = input as Partial<FeatureHitInput>;
	if (!candidate.features || !Array.isArray(candidate.features) || candidate.features.length !== 1 || !candidate.features[0].properties) {
		return undefined;
	}
  
  const feature = candidate.features[0];
  if (!feature || typeof feature !== "object" || !feature.properties) {
		return undefined;
	}

	return {
		kommunenummer: parseInt(getPropString(feature.properties, "kommunenummer") ?? "0", 10),
		navn_pri_1: getPropString(feature.properties, "navn_pri_1") ?? "?",
		navn_pri_2: getPropString(feature.properties, "navn_pri_2"),
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

function isKommunerLayer(input: unknown): boolean {
  return getLayerName(input) === "Kommuner";
}

export function createFeatureHitKommunerLayerContribution({
  React
}: FrontendHostApi): FrontendContribution {
	return {
		id: "feature-hit-kommuner-layer",
		purpose: "feature-hit-layer",
		shouldRender: isKommunerLayer,
		factory: () => {
			return function FeatureHitsKommunerLayer({ input }) {
				const parsed = parseFeatureHitInput(input);
				return parsed && (
					<>
            <p/>
						{parsed.navn_pri_1}{parsed.navn_pri_2 ? ` (${parsed.navn_pri_2})` : null},
            kommune #{parsed.kommunenummer} <br/>
					</>
				);
			};
		},
	};
}
