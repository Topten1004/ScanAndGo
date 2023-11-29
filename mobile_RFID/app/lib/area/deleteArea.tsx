import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"

const deleteArea = async (id: number | null) => {
  try {
    if (!id) return

    const response = await axios.delete(`${BACKEND_URL}/area/delete?id=${id}`)
    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default deleteArea
