import { useMutation, useQueryClient } from "react-query"
import http from "services/http-common"

const useLocationForm = () => {

    const queryClient = useQueryClient();

    const { mutate: createMutate } = useMutation({
        mutationFn: async ({
            name
        }: {
            name: string
        }) => {
            await http.post("/location/create", {
                name
            })

            queryClient.invalidateQueries('getAllLocation')
        }
    })

    const { mutate: deleteMutate } = useMutation({
        mutationFn: async (id: string) => {
            await http.delete(`/location/delete?id=${id}`)

            queryClient.invalidateQueries('getAllLocation')
        }
    })

    const { mutate: updateMutate } = useMutation({
        mutationFn: async ({
            id, name
        }: {
            id: number,
            name: string
        }) => {
            const res = await http.put(`/location/update?id=${id}`, {name})

            queryClient.invalidateQueries('getAllLocation')
        }
    })

    return {
        create: createMutate,
        delete: deleteMutate,
        update: updateMutate
    }
}

export default useLocationForm