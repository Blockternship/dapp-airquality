module.exports =
/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		var threw = true;
/******/ 		try {
/******/ 			modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/ 			threw = false;
/******/ 		} finally {
/******/ 			if(threw) delete installedModules[moduleId];
/******/ 		}
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 4);
/******/ })
/************************************************************************/
/******/ ({

/***/ 4:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(5);


/***/ }),

/***/ 5:
/***/ (function(module, exports) {

throw new Error("Module build failed: Error: Cannot find module 'babel-plugin-transform-class-properties' from '/Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality'\n    at Function.module.exports [as sync] (/Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/resolve/lib/sync.js:40:15)\n    at resolveStandardizedName (/Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/files/plugins.js:78:29)\n    at resolvePlugin (/Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/files/plugins.js:27:10)\n    at loadPlugin (/Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/files/plugins.js:35:18)\n    at createDescriptor (/Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/config-descriptors.js:152:21)\n    at /Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/config-descriptors.js:104:12\n    at Array.map (<anonymous>)\n    at createDescriptors (/Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/config-descriptors.js:103:27)\n    at createPluginDescriptors (/Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/config-descriptors.js:99:10)\n    at /Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/config-descriptors.js:50:19\n    at plugins (/Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/config-descriptors.js:40:25)\n    at mergeChainOpts (/Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/config-chain.js:296:68)\n    at /Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/config-chain.js:251:7\n    at buildRootChain (/Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/config-chain.js:85:20)\n    at loadPrivatePartialConfig (/Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/partial.js:41:53)\n    at Object.loadPartialConfig (/Users/meridianc4m1l0/Projects/WATER_COMPETITION/BlockternShip/gh-pages/dapp-airquality/node_modules/@babel/core/lib/config/partial.js:66:16)");

/***/ })

/******/ });