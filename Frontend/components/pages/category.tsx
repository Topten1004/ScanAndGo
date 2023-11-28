import React, { useContext, useRef, useState } from 'react'
import { useQuery } from 'react-query';
import http from 'services/http-common';
import useCategoryForm from 'components/cloud-words/useCategoryForm';
import { AiOutlineClose, AiFillEdit } from 'react-icons/ai'
import { useRouter } from 'next/router';
import { useAppDispatch, useAppSelector } from 'store/hooks';
import { setCategoryName } from 'store/slices/cloudwords.slice';
import { LanguageContext } from 'shared/context/language';

const CategoryPage = () => {
  const classes = {
    label: 'block mb-2 text-sm font-medium text-gray-900 dark:text-white',
    input: 'bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:border-blue-500 block w-80 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:border-blue-500 focus:outline-none',
    button: 'text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:outline-none font-medium rounded-lg text-sm px-5 py-2.5 text-center'
  }
  const { dictionary } = useContext(LanguageContext);
  const router = useRouter();
  const dispatch = useAppDispatch();
  const { create, delete: deleteOne, update } = useCategoryForm();
  const [name, setName] = useState<string>("");
  const [curId, setCurId] = useState(-1);
  const { account } = useAppSelector(state => state.auth);

  const getAllCategory: any = useQuery(
    ['getAllCategory'],
    () => {
      return http.get('/category/read')
    }
  )

  const allCategory = React.useMemo(() => {
    return getAllCategory.data ?
      getAllCategory.data.data : []
  }, [getAllCategory.data])

  const handleAdd = () => {
    create({ name });
    setName("");
  }

  const handleDelete = (id: string) => {
    deleteOne(id);
  }

  const handleEdit = (id: number, value: string) => {
    setCurId(id);
    setName(value);
  }

  const handleUpdate = () => {
    update({ id: curId, name })
    setCurId(-1);
    setName("");
  }

  const handleCancel = () => {
    setCurId(-1);
    setName("");
  }

  return (
    <div className='px-12 py-16 space-y-12'>
      <div className='flex items-center gap-8'>
        {account?.role != 3 && <input type='text' className={classes.input} value={name} onChange={(e) => setName(e.target.value)} />}
        {account?.role != 3 && (curId != -1 ? <>
          <button className={classes.button} onClick={handleUpdate}>{dictionary['update']}</button>
          <button className={classes.button} onClick={handleCancel}>{dictionary['cancel']}</button>
        </>
          : <button className={classes.button} onClick={handleAdd}>{dictionary['add']}</button>)}
      </div>
      <div className='flex flex-wrap gap-4'>
        {
          allCategory?.map((each: any, index: number) => (
            <div key={index} className={'hover:bg-green-500 transition-all duration-300 ease-in-out text-white rounded-md flex justify-between items-center hover:cursor-pointer ' + (!each.isUsed ? 'bg-blue-500' : 'bg-red-500')}>
              <div className='pl-8 pr-8 py-1' onClick={() => {
                dispatch(setCategoryName(each.name))
                router.push(`/category/${each.id}`)
              }}>
                {each.name}
              </div>
              {curId != each.id && account?.role != 3 && <div className='pr-4 py-1' onClick={() => handleEdit(each.id, each.name)}>
                <AiFillEdit className='hover:text-red-500 transition-all duration-300 ease-in-out hover:cursor-pointer' />
              </div>}
              {!each.isUsed && account?.role != 3 && <div className='pr-4 py-1'>
                <AiOutlineClose onClick={() => handleDelete(each.id)} className='hover:text-red-500 transition-all duration-300 ease-in-out hover:cursor-pointer' />
              </div>}
            </div>
          ))
        }
      </div>
    </div>
  )
}

export default CategoryPage