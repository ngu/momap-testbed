export {};

import type { FrontendExtension } from "@mareano-frontend/extensions/frontendExtensionsRegistry";
import { createLocationSearchRightMenuToolContribution } from "./location-search/rightMenuTool";
import { createLocationSearchRightMenuActivityContribution } from "./location-search/rightMenuActivity";

const frontendExtension: FrontendExtension = (frontendApi) => [
  createLocationSearchRightMenuToolContribution(frontendApi),
  createLocationSearchRightMenuActivityContribution(frontendApi)
];

(window as any).registerFrontendExtension(frontendExtension);
