import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"

const getAreas = async (areaId: number) => {
  try {
    const response = await axios.get(`${BACKEND_URL}/area/read?id=${areaId}`)
    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default getAreas
