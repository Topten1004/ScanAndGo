import { combineReducers, configureStore } from "@reduxjs/toolkit";
import thunk from 'redux-thunk' ;
import { persistStore, persistReducer } from "redux-persist";
import storage from 'redux-persist/lib/storage' ;

import appReducer from './slices/app.slice' ;
import authReducer from './slices/auth.slice' ;
import cloudwordsSlice from "./slices/cloudwords.slice";
import locationSlice from "./slices/location.slice";

const rootReducer = combineReducers({
    app: appReducer,
    auth: authReducer,
    cws: cloudwordsSlice,
    location: locationSlice
})

const persistConfig = {
    key: 'root',
    storage
}

const persistedReducer = persistReducer(persistConfig, rootReducer) ;

export const store = configureStore({
    reducer: persistedReducer,
    devTools: process.env.NODE_ENV !== 'production',
    middleware: [thunk]
})

export const persistor = persistStore(store)

export type RootState = ReturnType<typeof store.getState>