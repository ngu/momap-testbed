export {};

import type { FrontendExtension } from "@mareano-frontend/extensions/frontendExtensionsRegistry";
import { createLocationSearchRightMenuToolContribution } from "./location-search/rightMenuTool";
import { createLocationSearchRightMenuActivityContribution } from "./location-search/rightMenuActivity";
import { createFeatureHitsFylkerDetailsContribution, createFeatureHitsFylkerTitleContribution } from "./feature-hits/featureHitsFylker";
import { createFeatureHitKommunerLayerContribution } from "./feature-hits/featureHitsKommuner";

const frontendExtension: FrontendExtension = (frontendApi) => [
  createLocationSearchRightMenuToolContribution(frontendApi),
  createLocationSearchRightMenuActivityContribution(frontendApi),
  createFeatureHitsFylkerTitleContribution(frontendApi),
  createFeatureHitsFylkerDetailsContribution(frontendApi),
  createFeatureHitKommunerLayerContribution(frontendApi),
];

(window as any).registerFrontendExtension(frontendExtension);
