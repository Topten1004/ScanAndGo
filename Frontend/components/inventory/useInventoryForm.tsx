import { useMutation, useQueryClient } from "react-query"
import http from "services/http-common"

const useInventoryForm = () => {

    const queryClient = useQueryClient();

    const { mutate: createMutate, isLoading: createIsLoading } = useMutation({
        mutationFn: async ({
            category,
            subcategory,
            item,
            location,
            sublocation,
            purchaseDate,
            lastDate,
            refClient
        }: {
            category: string,
            subcategory: string,
            item: string,
            location: string,
            sublocation: string,
            purchaseDate: string,
            lastDate: string,
            refClient: string
        }) => {
            await http.post("/inventory/create", {
                category,
                subcategory,
                item,
                location,
                sublocation,
                purchaseDate,
                lastDate,
                refClient
            })

            // queryClient.invalidateQueries('getAllLocation')
        }
    })

    return {
        create: createMutate,
        createIsLoading: createIsLoading
    }
}

export default useInventoryForm