import { ISession } from 'types/interface';
import { createSlice, PayloadAction } from '@reduxjs/toolkit' ;
import { RootState } from 'store';

type AppState = {
    sessionExpired: boolean
    session: ISession | null
}

const initialState: AppState = {
    sessionExpired: true,
    session: null
}

export const app = createSlice({
    name: 'app',
    initialState,
    reducers: {
        setSessionExpired: (state) => {
            state.sessionExpired = true;
            state.session = null;
        },
        setSession: (state, action: PayloadAction<ISession>) => {
            state.session = action.payload;
            state.sessionExpired = false;
        }
    }
})

export const { setSessionExpired, setSession } = app.actions ;

export const getSession = (state: RootState) => state.app.session ;

export default app.reducer