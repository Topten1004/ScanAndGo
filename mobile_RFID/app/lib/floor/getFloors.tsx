import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"

const getFloors = async (areaId: number) => {
  try {
    const response = await axios.get(`${BACKEND_URL}/floor/read?id=${areaId}`)
    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default getFloors
