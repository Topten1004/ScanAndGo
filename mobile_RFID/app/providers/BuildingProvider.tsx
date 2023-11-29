import { createContext, useContext, useMemo, useState, useEffect, useCallback } from "react"
import getBuildings from "../lib/building/getBuildings"
import createBuilding from "../lib/building/createBuilding"
import { IBuilding } from "../types/build"
import deleteBuilding from "../lib/building/deleteBuilding"
import updateBuilding from "../lib/building/updateBuilding"

const BuildingContext: any = createContext(null)

const BuildingProvider = ({ children }: any) => {
  const [buildings, setBuildings] = useState([])
  const [loading, setLoading] = useState(false)
  const [buildingName, setBuildingName] = useState("")
  const [isEditMode, setIsEditMode] = useState(false)
  const [editBuildId, setEditBuildId] = useState<number | null>(null)

  const addNewBuild = async () => {
    const response = await createBuilding(buildingName)
    setBuildingName("")
    if (response?.error) return
    await getBuildingList()
  }

  const updateBuild = async () => {
    if (!editBuildId) return
    const response = await updateBuilding({
      id: editBuildId,
      name: buildingName,
    })

    setIsEditMode(false)
    setBuildingName("")

    if (response?.error) return
    await getBuildingList()
  }

  const deleteBuild = async (id: number | null) => {
    const response = await deleteBuilding(id)
    if (response?.error) return
    await getBuildingList()
  }

  const openEditMode = (data: IBuilding) => {
    setIsEditMode(true)
    setBuildingName(data.name)
    setEditBuildId(data.id)
  }

  const closeEditMode = () => {
    setIsEditMode(false)
    setBuildingName("")
    setEditBuildId(null)
  }

  const getBuildingList = useCallback(async () => {
    setLoading(true)
    const response = await getBuildings()
    setBuildings(response)
    setLoading(false)
  }, [])

  useEffect(() => {
    getBuildingList()
  }, [getBuildingList])

  const value = useMemo(
    () => ({
      buildings,
      getBuildingList,
      buildingName,
      setBuildingName,
      addNewBuild,
      loading,
      isEditMode,
      setIsEditMode,
      openEditMode,
      updateBuild,
      deleteBuild,
      closeEditMode,
    }),
    [
      buildings,
      getBuildingList,
      buildingName,
      setBuildingName,
      addNewBuild,
      loading,
      isEditMode,
      setIsEditMode,
      openEditMode,
      updateBuild,
      deleteBuild,
      closeEditMode,
    ],
  )

  return <BuildingContext.Provider value={value}>{children}</BuildingContext.Provider>
}

export const useBuilding = () => {
  const context: any = useContext(BuildingContext)
  if (!context) {
    throw new Error("useBuilding must be used within a BuildingProvider")
  }
  return context
}

export default BuildingProvider
