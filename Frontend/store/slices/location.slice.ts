import { createSlice, PayloadAction } from '@reduxjs/toolkit';

type LocationState = {
    locationName: string
}

const initialState: LocationState = {
    locationName: ""
}

export const locationSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        setLocationName: (state, action: PayloadAction<string>) => {
            const temp = action.payload;
            state.locationName = temp;
        }
    }
})

export const {
    setLocationName
} = locationSlice.actions

export default locationSlice.reducer
