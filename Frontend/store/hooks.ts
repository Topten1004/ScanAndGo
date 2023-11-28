import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux' ;
import { RootState } from 'store';

export const useAppDispatch: () => any = useDispatch ;
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector ;