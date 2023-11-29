import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"
import { ILocation } from "../../types/location"

const updateLocation = async (data: ILocation) => {
  try {
    const response = await axios.put(`${BACKEND_URL}/detaillocation/update?id=${data.id}`, {
      name: data.name,
    })
    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default updateLocation
