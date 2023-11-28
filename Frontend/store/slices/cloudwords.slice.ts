import { createSlice, PayloadAction } from '@reduxjs/toolkit';

type CloudWordsState = {
    categoryName: string
    subcategoryName: string
}

const initialState: CloudWordsState = {
    categoryName: "",
    subcategoryName: ""
}

export const cloudwordsSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        setCategoryName: (state, action: PayloadAction<string>) => {
            const temp = action.payload;
            state.categoryName = temp;
        },
        setSubCategoryName: (state, action: PayloadAction<string>) => {
            const temp = action.payload;
            state.subcategoryName = temp;
        }
    }
})

export const {
    setCategoryName,
    setSubCategoryName
} = cloudwordsSlice.actions

export default cloudwordsSlice.reducer
