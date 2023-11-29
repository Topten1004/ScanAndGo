import { useMutation, useQueryClient } from "react-query"
import http from "../../service/http-common"

const useItemForm = () => {
  const queryClient = useQueryClient()

  const { mutate: createMutate } = useMutation({
    mutationFn: async ({
      subCategoryId,
      name,
    }: {
      subCategoryId: string | string[] | undefined
      name: string
    }) => {
      await http.post("/item/create", {
        subCategoryId,
        name,
      })

      queryClient.invalidateQueries("getAllItem")
    },
  })

  const { mutate: deleteMutate } = useMutation({
    mutationFn: async (id: string) => {
      await http.delete(`/item/delete?id=${id}`)

      queryClient.invalidateQueries("getAllItem")
    },
  })

  const { mutate: updateMutate } = useMutation({
    mutationFn: async ({ id, name }: { id: number; name: string }) => {
      const res = await http.put(`/item/update?id=${id}`, { name })

      queryClient.invalidateQueries("getAllItem")
    },
  })

  return {
    create: createMutate,
    delete: deleteMutate,
    update: updateMutate,
  }
}

export default useItemForm
