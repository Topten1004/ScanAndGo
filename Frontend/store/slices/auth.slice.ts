import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { eraseCookie } from 'shared/helper/tokens';
import { RootState } from 'store';
import { IUser } from 'types/interface';

type AuthState = {
    account: IUser | null
    token: string
}

const initialState: AuthState = {
    account: null,
    token: ""
}

export const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        setSelectedAccount: (state, action: PayloadAction<IUser>) => {
            const singleUser = action.payload;
            state.account = singleUser;
        },
        setToken: (state, action: PayloadAction<string>) => {
            const payload = action.payload;
            state.token = payload;  
        },
        logout: state => {
            eraseCookie('token')
            state.account = null
        }
    }
})

export const {
    setSelectedAccount,
    setToken,
    logout
} = authSlice.actions

export const selectUser = (state: RootState) => state.auth.account

export default authSlice.reducer
