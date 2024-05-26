/**
 * name: pinia-persistedstate-plugin
 * version: v0.1.0
 * description: A pinia plugin like vuex-persistedstate.
 * author: xiaofei
 * homepage: https://github.com/l-x-f/pinia-persistedstate-plugin#readme
 */
(function(r,c){typeof exports=="object"&&typeof module!="undefined"?c(exports):typeof define=="function"&&define.amd?define(["exports"],c):(r=typeof globalThis!="undefined"?globalThis:r||self,c(r.PiniaPersistedstatePlugin={}))})(this,function(r){"use strict";function c(e,s,u){const l=console.groupCollapsed||console.group;try{l(e,new Date().toLocaleString()),console.log("mutation",s),console.log("state",u),console.groupEnd()}catch(i){console.log("\u2014\u2014 log end \u2014\u2014")}}function f(e){const s=(e==null?void 0:e.storage)||window&&window.localStorage,u=(e==null?void 0:e.key)||"pinia",l=(e==null?void 0:e.logger)||!1,i=(o,t)=>{const n=t.getItem?t.getItem(o):t.get(o);try{return typeof n=="string"?JSON.parse(n):typeof n=="object"?n:void 0}catch(d){}},y=(o,t,n)=>n.setItem?n.setItem(o,JSON.stringify(t)):n.set(o,JSON.stringify(t));return o=>{const t=o.store,n=`${u}-${t.$id}`,d=i(n,s);d&&t.$patch(d),t.$subscribe((g,a)=>{l&&c(g.storeId,g,a);const S=`${u}-${g.storeId}`;y(S,a,s)})}}r.createPersistedState=f,Object.defineProperty(r,"__esModule",{value:!0}),r[Symbol.toStringTag]="Module"});
