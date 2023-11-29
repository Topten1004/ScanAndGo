import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"

const getLocations = async (floorId: number) => {
  try {
    const response = await axios.get(`${BACKEND_URL}/detaillocation/read?id=${floorId}`)
    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default getLocations
