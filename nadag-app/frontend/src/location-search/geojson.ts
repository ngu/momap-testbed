export const LOCATION_SEARCH_TOOL_ID = "location-search.tool";
export const LOCATION_SEARCH_ACTIVITY_ID = "location-search.activity";

export type GeoJsonFeature = {
	type: "Feature";
	geometry?: Record<string, unknown> | null;
	properties?: Record<string, unknown> | null;
	crs?: {
		type?: string | null;
		properties?: {
			name?: string | null;
		};
	} | null;
};

export type GeoJsonFeatureCollection = {
	type: "FeatureCollection";
	name?: string | null;
	crs?: {
		type?: string | null;
		properties?: {
			name?: string | null;
		};
	} | null;
	features: GeoJsonFeature[];
};
