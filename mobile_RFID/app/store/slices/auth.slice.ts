import { createSlice, PayloadAction } from "@reduxjs/toolkit"
import { IUser } from "../../types/interface"

interface IAuth {
  account: IUser | null
  token: string
}

const initialState: IAuth = {
  account: null,
  token: "",
}

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setAccount: (state, action: PayloadAction<any>) => {
      state.account = action.payload
    },
    setToken: (state, action: PayloadAction<any>) => {
      state.token = action.payload
    },
  },
})

export const { setAccount, setToken } = authSlice.actions

export default authSlice.reducer
