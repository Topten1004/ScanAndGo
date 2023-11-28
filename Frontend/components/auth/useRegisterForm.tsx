import { useMutation } from "react-query"
import http from "services/http-common"


const useRegisterForm = () => {
    const { mutate, isLoading, error } = useMutation({
        mutationFn: async ({
            username,
            password
        }: {
            username: string
            password: string
        }) => {
            let res: any = await http.post("/user/register", {
                username,
                password
            })

            console.log(res);
        }
    })

    return {
        register: mutate,
        isLoading,
        error
    }
}

export default useRegisterForm