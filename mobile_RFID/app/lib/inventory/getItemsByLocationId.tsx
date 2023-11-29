import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"

const getItemsByLocationId = async (currentLocationId: number) => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/inventory/detaillocation/read?id=${currentLocationId}`,
    )
    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default getItemsByLocationId
