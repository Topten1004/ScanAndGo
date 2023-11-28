import { useRouter } from "next/router"
import { useContext } from "react"
import { useMutation } from "react-query"
import { toast } from "react-toastify"
import http from "services/http-common"
import { LanguageContext } from "shared/context/language"
import { setCookie } from "shared/helper/tokens"
import { useAppDispatch } from "store/hooks"
import { setSelectedAccount, setToken } from "store/slices/auth.slice"


const useLoginForm = () => {

    const { dictionary } = useContext(LanguageContext);

    const router = useRouter();
    const dispatch = useAppDispatch();

    const { mutate, isLoading, error } = useMutation({
        mutationFn: async ({
            username,
            password
        }: {
            username: string
            password: string
        }) => {
            let res: any = await http.post("/user/signin", {
                username,
                password
            })

            if (res.data.status == -1) {
                toast.error("User Not Found");
            } else if (res.data.status == 0) {
                toast.error("Password Not correct");
            } else if (res.data.user.role == 0) {
                toast.warn("Your account is pending now");
            } else if (res.data.status == 1) {
                toast.success("SignIn Successfully");
                setCookie("token", res.data.access_token);
                dispatch(setToken(res.data.access_token));
                dispatch(setSelectedAccount(res.data.user));
                router.push("/category")
            }
        }
    })

    return {
        login: mutate,
        isLoading,
        error
    }
}

export default useLoginForm