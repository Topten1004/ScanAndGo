import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"

const getItems = async (categoryId: number) => {
  try {
    const response = await axios.get(`${BACKEND_URL}/item/read?id=${categoryId}`)

    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default getItems
