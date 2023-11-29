import { combineReducers, configureStore } from "@reduxjs/toolkit"
import thunk from "redux-thunk"

import authReducer from "./slices/auth.slice"

const rootReducer = combineReducers({
  auth: authReducer,
})

export const store = configureStore({
  reducer: rootReducer,
  middleware: [thunk],
})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch
