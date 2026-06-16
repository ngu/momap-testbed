export {};

import type { FrontendExtension } from "@mareano-frontend/extensions/frontendExtensionsRegistry";
import { createLocationSearchRightMenuToolContribution } from "./location-search/rightMenuTool";
import { createLocationSearchRightMenuActivityContribution } from "./location-search/rightMenuActivity";
import { createFeatureHitsFylkerDetailsContribution, createFeatureHitsFylkerTitleContribution } from "./feature-hits/featureHitsFylker";
import { createFeatureHitKommunerLayerContribution } from "./feature-hits/featureHitsKommuner";
import en from "./locales/en.json";
import nb from "./locales/nb.json";

const SAMPLE_EXTENSION_NAMESPACE = "sample.extension";

const frontendExtension: FrontendExtension = (frontendApi) => {
  frontendApi.i18nSupport.registerTranslations({
    namespace: SAMPLE_EXTENSION_NAMESPACE,
    resources: {
      en,
      nb,
    },
  });

  return [
    createLocationSearchRightMenuToolContribution(frontendApi, SAMPLE_EXTENSION_NAMESPACE),
    createLocationSearchRightMenuActivityContribution(frontendApi, SAMPLE_EXTENSION_NAMESPACE),
    createFeatureHitsFylkerTitleContribution(frontendApi),
    createFeatureHitsFylkerDetailsContribution(frontendApi),
    createFeatureHitKommunerLayerContribution(frontendApi),
  ];
};

(window as any).registerFrontendExtension(frontendExtension);
