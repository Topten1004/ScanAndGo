import { createContext, useContext, useMemo, useState, useEffect, useCallback } from "react"
import createArea from "../lib/area/createArea"
import { IArea } from "../types/area"
import deleteArea from "../lib/area/deleteArea"
import updateArea from "../lib/area/updateArea"
import { useNavigation } from "@react-navigation/native"
import getAreas from "../lib/area/getAreas"

const AreaContext: any = createContext(null)

const AreaProvider = ({ children }: any) => {
  const navigation = useNavigation()

  const states: any = navigation.getState()

  const [areas, setAreas] = useState([])
  const [loading, setLoading] = useState(false)
  const [areaName, setAreaName] = useState("")
  const [isEditMode, setIsEditMode] = useState(false)
  const [editAreaId, setEditAreaId] = useState<number | null>(null)

  const buildingId = useMemo(() => {
    return states.routes.find((route: any) => route.name === "Area").params.buildingId
  }, [states])

  const addNewArea = async () => {
    const response = await createArea(areaName)
    setAreaName("")
    if (response?.error) return
    getAreaList()
  }

  const updateAreaById = async () => {
    if (!editAreaId) return
    const response = await updateArea({
      id: editAreaId,
      name: areaName,
    })

    setIsEditMode(false)
    setAreaName("")

    if (response?.error) return
    getAreaList()
  }

  const deleteAreaById = async (id: number | null) => {
    const response = await deleteArea(id)
    if (response?.error) return
    getAreaList()
  }

  const openEditMode = (data: IArea) => {
    setIsEditMode(true)
    setAreaName(data.name)
    setEditAreaId(data.id)
  }

  const closeEditMode = () => {
    setIsEditMode(false)
    setAreaName("")
    setEditAreaId(null)
  }

  const getAreaList = useCallback(async () => {
    setLoading(true)
    const response = await getAreas(buildingId)
    if (response?.error) return
    setAreas(response)
    setLoading(false)
  }, [buildingId])

  useEffect(() => {
    getAreaList()
  }, [getAreaList])

  const value = useMemo(
    () => ({
      areas,
      getAreaList,
      areaName,
      setAreaName,
      addNewArea,
      loading,
      isEditMode,
      setIsEditMode,
      openEditMode,
      updateAreaById,
      deleteAreaById,
      closeEditMode,
      buildingId
    }),
    [
      areas,
      getAreaList,
      areaName,
      setAreaName,
      addNewArea,
      loading,
      isEditMode,
      setIsEditMode,
      openEditMode,
      updateAreaById,
      deleteAreaById,
      closeEditMode,
      buildingId
    ],
  )

  return <AreaContext.Provider value={value}>{children}</AreaContext.Provider>
}

export const useArea = () => {
  const context: any = useContext(AreaContext)
  if (!context) {
    throw new Error("useArea must be used within a AreaProvider")
  }
  return context
}

export default AreaProvider
