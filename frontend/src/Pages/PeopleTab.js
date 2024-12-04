import React, { useState, useEffect, useCallback } from 'react';
import { Spin, notification } from 'antd';
import debounce from 'lodash/debounce';

import SearchBar from '../Components/SearchBar';
import TableView from '../Components/TableView';
import { fetchPeopleData } from '../Utils/apiCalls';

const PeopleTab = () => {
  const [peopleData, setPeopleData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [filterField, setFilterField] = useState('name');
  const [filterValue, setFilterValue] = useState('');
  const [sortField, setSortField] = useState('');
  const [sortOrder, setSortOrder] = useState('');
  const [tablePagination, setTablePagination] = useState({
    currentPage: 1,
    pageSize: 15,
    total: 0,
  });

  const peopleColumns = [
    { title: 'Name', dataIndex: 'name', key: 'name', ellipsis: true, sorter: true, width: 150 },
    { title: 'Gender', dataIndex: 'gender', key: 'gender', ellipsis: true, width: 100 },
    { title: 'Birth Year', dataIndex: 'birth_year', key: 'birth_year', ellipsis: true, width: 100 },
    { title: 'Height', dataIndex: 'height', key: 'height', ellipsis: true, width: 80 },
    { title: 'Mass', dataIndex: 'mass', key: 'mass', ellipsis: true, width: 80 },
    { title: 'Hair Color', dataIndex: 'hair_color', key: 'hair_color', ellipsis: true, width: 120 },
    { title: 'Skin Color', dataIndex: 'skin_color', key: 'skin_color', ellipsis: true, width: 120 },
    { title: 'Eye Color', dataIndex: 'eye_color', key: 'eye_color', ellipsis: true, width: 120 },
    { title: 'Created', dataIndex: 'created', key: 'created', ellipsis: true, sorter: true, width: 200, render: (value) => new Date(value).toLocaleString() },
    { title: 'Edited', dataIndex: 'edited', key: 'edited', ellipsis: true, sorter: true, width: 200, render: (value) => new Date(value).toLocaleString() },
  ];

  const peopleSearchFields = [
    { label: 'Name', value: 'name'},
    { label: 'Gender', value: 'gender'},
    { label: 'Hair Color', value: 'hairColor'},
    { label: 'Skin Color', value: 'skinColor'},
    { label: 'Eye Color', value: 'eyeColor'}
  ]

  const fetchPeople = useCallback(
    debounce(async (filterField, filterValue, sortField, sortOrder, page, pageSize) => {
      setLoading(true);
      try {
        console.log(`/api/people?filterField=${filterField}&filterValue=${filterValue}&sort=${sortField}&order=${sortOrder}&page=${page}&size=${pageSize}`);
        const {currentPage, numberOfPages, data} = await fetchPeopleData(filterField, filterValue, sortField, sortOrder, page, pageSize);
        setPeopleData(data);
        setTablePagination({
          ...tablePagination,
          currentPage: currentPage > numberOfPages ? numberOfPages : currentPage,
          total: numberOfPages * pageSize
        });
      } catch (error) {
        setPeopleData([]);
        notification.error({
          message: 'Fetch Error',
          description: 'Could not fetch people data.',
        });
      } finally {
        setLoading(false);
      }
    }, 800),
    []
  );

  useEffect(() => {
    fetchPeople(filterField, filterValue, sortField, sortOrder, tablePagination.currentPage, tablePagination.pageSize);
  }, [filterValue, sortField, sortOrder, tablePagination.currentPage, tablePagination.pageSize]);

  const handleTableChange = (pagination, filters, sorter) => {
    const { current, pageSize } = pagination;
    if (current !== tablePagination.currentPage || pageSize !== tablePagination.pageSize) {
      setTablePagination({
        ...tablePagination,
        currentPage: current
      });
    }
    if (sorter.order) {
      setSortField(sorter.field);
      setSortOrder(sorter.order === 'ascend' ? 'asc' : 'desc');
    } else {
      setSortField('');
      setSortOrder('');
    }
  };

  const handleFilterFieldChange = (value) => {
    setFilterField(value);
    setFilterValue('');
  }

  return (
    <Spin spinning={loading}>
      <SearchBar
        filterField={filterField}
        filterValue={filterValue}
        onFieldChange={handleFilterFieldChange}
        onValueChange={setFilterValue}
        searchFields={peopleSearchFields}
      />
      <TableView
        data={peopleData}
        columns={peopleColumns}
        onTableChange={handleTableChange}
        tablePagination={tablePagination}
      />
    </Spin>
  );
};

export default PeopleTab;
