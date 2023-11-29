import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"

const createBuilding = async (name: string) => {
  try {
    const response = await axios.post(`${BACKEND_URL}/building/create`, {
      name,
    })
    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default createBuilding
