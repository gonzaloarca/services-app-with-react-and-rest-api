import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import detector from 'i18next-browser-languagedetector';
import translationES from './i18n/es.json';
import translationEN from './i18n/en.json';
import moment from 'moment';
// the translations
// (tip move them in a JSON file and import them,
// or even better, manage them via a UI: https://react.i18next.com/guides/multiple-translation-files#manage-your-translations-with-a-management-gui)
const resources = {
  es: {
    translation: translationES,
  },
  en: {
    translation: translationEN,
  },
};

i18n
  // detect user language
  // learn more: https://github.com/i18next/i18next-browser-languageDetector
  .use(detector)
  // pass the i18n instance to react-i18next.
  .use(initReactI18next)
  // init i18next
  // for all options read: https://www.i18next.com/overview/configuration-options
  .init({
    fallbackLng: 'es',
    react: {
      useSuspense: false,
    },
    debug: true,
    resources,
    interpolation: {
      escapeValue: false, // not needed for react as it escapes by default
      format: (value, rawFormat, lng) => {
        const [format, ...additionalValues] = rawFormat
          .split(',')
          .map((v) => v.trim());
        if (format === 'price')
          return Intl.NumberFormat(lng, {
            style: 'currency',
            currency: additionalValues[0],
          }).format(value);
        if (value instanceof Date) return moment(value).format(rawFormat);
        return value;
      },
    },
  });

export default i18n;
