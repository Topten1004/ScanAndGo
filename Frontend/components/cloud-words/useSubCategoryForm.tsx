import { useMutation, useQueryClient } from "react-query"
import http from "services/http-common"

const useSubCategoryForm = () => {

    const queryClient = useQueryClient();

    const { mutate: createMutate } = useMutation({
        mutationFn: async ({
            categoryId,
            name
        }: {
            categoryId: string | string[] | undefined,
            name: string
        }) => {
            await http.post("/subcategory/create", {
                categoryId,
                name
            })

            queryClient.invalidateQueries('getAllSubCategory')
        }
    })

    const { mutate: deleteMutate } = useMutation({
        mutationFn: async (id: string) => {
            await http.delete(`/subcategory/delete?id=${id}`)

            queryClient.invalidateQueries('getAllSubCategory')
        }
    })

    const { mutate: updateMutate } = useMutation({
        mutationFn: async ({
            id, name
        }: {
            id: number,
            name: string
        }) => {
            const res = await http.put(`/subcategory/update?id=${id}`, {name})

            queryClient.invalidateQueries('getAllSubCategory')
        }
    })

    return {
        create: createMutate,
        delete: deleteMutate,
        update: updateMutate
    }
}

export default useSubCategoryForm