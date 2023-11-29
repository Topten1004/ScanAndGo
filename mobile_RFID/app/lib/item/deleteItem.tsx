import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"

const deleteItem = async (id: number | null) => {
  try {
    if (!id) return

    const response = await axios.delete(`${BACKEND_URL}/item/delete?id=${id}`)
    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default deleteItem
