import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"

const getCategories = async () => {
  try {
    const response = await axios.get(`${BACKEND_URL}/category/read`)

    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default getCategories
