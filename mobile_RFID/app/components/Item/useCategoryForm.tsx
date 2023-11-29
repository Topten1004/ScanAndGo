import { useMutation, useQueryClient } from "react-query"
import http from "../../service/http-common"
import { showToastWithGravity } from "../../shared/helper/toast"

const useCategoryForm = () => {
  const queryClient = useQueryClient()

  const { mutate: createMutate } = useMutation({
    mutationFn: async ({ name }: { name: string }) => {
      const res = await http.post("/category/create", {
        name,
      })

      if (res?.data.status == 1) queryClient.invalidateQueries("getAllCategory")
      else if (res?.data.status == 0) showToastWithGravity("The Category is already existed!")
    },
  })

  const { mutate: deleteMutate } = useMutation({
    mutationFn: async (id: string) => {
      await http.delete(`/category/delete?id=${id}`)

      queryClient.invalidateQueries("getAllCategory")
    },
  })

  const { mutate: updateMutate } = useMutation({
    mutationFn: async ({ id, name }: { id: number; name: string }) => {
      const res = await http.put(`/category/update?id=${id}`, { name })

      queryClient.invalidateQueries("getAllCategory")
    },
  })

  return {
    create: createMutate,
    delete: deleteMutate,
    update: updateMutate,
  }
}

export default useCategoryForm
