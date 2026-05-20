export {};

import type { FrontendExtension } from "../../../../mareano-frontend/src/extensions/frontendExtensionsRegistry";
import { createLocationSearchRightMenuToolContribution } from "./location-search/rightMenuTool";
import { createLocationSearchRightMenuActivityContribution } from "./location-search/rightMenuActivity";

const nadagFrontendExtension: FrontendExtension = (frontendApi) => [
  createLocationSearchRightMenuToolContribution(frontendApi),
  createLocationSearchRightMenuActivityContribution(frontendApi)
];

(window as any).registerFrontendExtension(nadagFrontendExtension);
