export const NADAG_MENU_ID = "nadag.nadag-search";

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
