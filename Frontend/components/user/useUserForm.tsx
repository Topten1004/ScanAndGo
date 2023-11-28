import { useMutation, useQueryClient } from "react-query"
import http from "services/http-common"

const useUserForm = () => {

    const queryClient = useQueryClient();

    const { mutate: deleteMutate } = useMutation({
        mutationFn: async (id: number) => {
            await http.delete(`/user/delete?id=${id}`)

            queryClient.invalidateQueries('getAllUser')
        }
    })

    const { mutate: updateMutate } = useMutation({
        mutationFn: async ({
            id, role
        }: {
            id: number | undefined,
            role: number | undefined
        }) => {
            const res = await http.put(`/user/update?id=${id}`, {role})

            queryClient.invalidateQueries('getAllUser')
        }
    })

    return {
        delete: deleteMutate,
        update: updateMutate
    }
}

export default useUserForm