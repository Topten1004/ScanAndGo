import React, { useContext, useEffect, useState } from 'react'
import { useQuery } from 'react-query'
import { AiFillEdit, AiOutlineCheck } from 'react-icons/ai';
import { MdDelete, MdOutlineCancel } from 'react-icons/md'
import { IRole, IUser } from 'types/interface'
import http from 'services/http-common'
import useUserForm from 'components/user/useUserForm';
import { LanguageContext } from 'shared/context/language';

const UserPage = () => {

  const { dictionary } = useContext(LanguageContext);
  const { update, delete: deleteUser } = useUserForm();
  const [userList, setUserList] = useState<IUser[]>([]);
  const [curUser, setCurUser] = useState<IUser>();

  const getAllUser: any = useQuery(
    ['getAllUser'],
    () => {
      return http.get(`/user/alluser`)
    }
  )

  const allUser = React.useMemo(() => {
    return getAllUser.data ?
      getAllUser.data.data ?
        getAllUser.data.data.users : [] : []
  }, [getAllUser.data])

  useEffect(() => {
    if (allUser)
      setUserList(allUser)
  }, [allUser])

  const getAllRole: any = useQuery(
    ['getAllRole'],
    () => {
      return http.get(`/role/read`)
    }
  )

  const allRole = React.useMemo(() => {
    return getAllRole.data ?
      getAllRole.data.data : []
  }, [getAllRole.data])

  const handleEdit = (index: number) => {
    setCurUser(userList[index]);
  }

  const handleUpdate = () => {
    update({id: curUser?.id, role: curUser?.role})
    setCurUser(undefined);
  }

  const handleDelete = (id: number) => {
    deleteUser(id)
  }

  const handleCancel = () => {
    setCurUser(undefined);
  }

  return (
    <div className='px-12 py-16 space-y-12'>
      <div className="relative overflow-x-auto">
        <table className="w-full text-sm text-left text-gray-500 dark:text-gray-400">
          <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
            <tr>
              <th scope="col" className="px-6 py-3">
                {dictionary['no']}
              </th>
              <th scope="col" className="px-6 py-3">
                {dictionary['username']}
              </th>
              <th scope="col" className="px-6 py-3">
                {dictionary['role']}
              </th>
              <th scope="col" className="px-6 py-3">
                {dictionary['options']}
              </th>
            </tr>
          </thead>
          <tbody>
            {
              userList?.map((user: IUser, index: number) => (
                <tr key={index} className="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                  <th scope="row" className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
                    {index + 1}
                  </th>
                  <td className="px-6 py-4">
                    {user.username}
                  </td>
                  <td className="px-6 py-4">
                    {
                      curUser?.id == user.id ?
                        <select value={curUser.role} className='border border-gray-300 pl-2 py-1 w-32 rounded-md' onChange={(e) => setCurUser({...curUser, role: Number(e.target.value)})}>
                          {
                            allRole.map((role: IRole, index: number) => (
                              <option key={index} value={role.id}>{role.name}</option>
                            ))
                          }
                        </select>
                      : user.role == 0 ? 'no role' : <p>{allRole?.filter((item: IRole) => item.id == user.role)[0]?.name}</p>
                    }
                  </td>
                  <td className="px-6 py-4 flex gap-4">
                    {curUser?.id == user.id ? <AiOutlineCheck className='text-green-500 hover:cursor-pointer' size={20} onClick={handleUpdate} />
                      : <AiFillEdit className='text-blue-500 hover:cursor-pointer' size={20} onClick={() => handleEdit(index)} />}
                    {curUser?.id == user.id ? <MdOutlineCancel className='text-red-500 hover:cursor-pointer' size={20} onClick={handleCancel} />
                    : <MdDelete className='text-red-500 hover:cursor-pointer' size={20} onClick={() => handleDelete(user.id)} />}
                  </td>
                </tr>
              ))
            }
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default UserPage