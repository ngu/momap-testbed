export const LOCATION_SEARCH_TOOL_ID = "location-search.tool";
export const LOCATION_SEARCH_ACTIVITY_ID = "location-search.activity";

import type {
	GeoJsonFeature as MapGeoJsonFeature,
	GeoJsonFeatureCollection as MapGeoJsonFeatureCollection,
} from "@mareano-frontend/extensions/mapNavigateEvent";

export type GeoJsonFeature = MapGeoJsonFeature;

export type GeoJsonFeatureCollection = MapGeoJsonFeatureCollection & {
	name?: string | null;
};
