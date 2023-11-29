import { useMutation, useQueryClient } from "react-query"
import http from "../../service/http-common"

const useSubLocationForm = () => {
  const queryClient = useQueryClient()

  const { mutate: createMutate } = useMutation({
    mutationFn: async ({
      locationId,
      name,
      imgData,
    }: {
      locationId: string | string[] | undefined
      name: string
      imgData: string
    }) => {
      await http.post("/sublocation/create", {
        locationId,
        name,
        imgData,
      })

      queryClient.invalidateQueries("getAllSubLocation")
    },
  })

  const { mutate: deleteMutate } = useMutation({
    mutationFn: async (id: string) => {
      await http.delete(`/sublocation/delete?id=${id}`)

      queryClient.invalidateQueries("getAllSubLocation")
    },
  })

  const { mutate: updateMutate } = useMutation({
    mutationFn: async ({ id, name }: { id: number; name: string }) => {
      const res = await http.put(`/sublocation/update?id=${id}`, { name })

      queryClient.invalidateQueries("getAllSubLocation")
    },
  })

  return {
    create: createMutate,
    delete: deleteMutate,
    update: updateMutate,
  }
}

export default useSubLocationForm
