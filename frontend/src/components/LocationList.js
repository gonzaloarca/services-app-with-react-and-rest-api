import {
  Checkbox,
  Chip,
  Divider,
  InputAdornment,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  makeStyles,
  TextField,
} from '@material-ui/core';
import { LocationOn, Search } from '@material-ui/icons';
import React, { useContext, useEffect, useRef, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { ConstantDataContext } from '../context';
import { themeUtils } from '../theme';
import Fuse from 'fuse.js';
const useStyles = makeStyles((theme) => ({
  container: {
    width: '70%',
    margin: '0 auto',
  },
  list: {
    backgroundColor: 'white',
    height: 390,
    overflowY: 'auto',
    boxShadow: 'inset 0 -20px 10px -10px rgba(0,0,0,0.1)',
    borderRadius: '0 0 10px 10px',
    border: '2px solid #f1f1f1',
    margin: '0 auto',
  },
  searchInput: {
    fontSize: themeUtils.fontSizes.sm,
    fontWeight: 500,
  },
  filterImage: {
    height: '40%',
    width: '40%',
    objectFit: 'contain',
    marginBottom: 10,
  },
  noResultsText: {
    textAlign: 'center',
    width: '80%',
    WebkitLineClamp: 3,
    display: '-webkit-box',
    WebkitBoxOrient: 'vertical',
    overflowWrap: 'break-word',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
  },
  selections: {
    padding: 10,
    minHeight: 120,
    boxShadow: themeUtils.shadows.containerShadow,
    borderRadius: 10,
    marginBottom: 20,
  },
}));

const LocationList = ({
  multiple = false,
  name = '',
  initial = [],
  setFieldValue = null,
}) => {
  const classes = useStyles();
  const [checked, setChecked] = useState(initial === null ? [] : initial);
  const [filter, setFilter] = useState('');
  const { t } = useTranslation();
  const fuse = useRef(null);
  const { zones } = useContext(ConstantDataContext);

  useEffect(() => {
    if (zones) {
      fuse.current = new Fuse(zones, {
        includeScore: false,
        keys: ['description'],
      });
    }
  }, [zones]);

  const handleToggle = (value) => () => {
    const currentIndex = checked.indexOf(value);
    const newChecked = [...checked];

    if (currentIndex === -1) {
      newChecked.push(value);
    } else {
      newChecked.splice(currentIndex, 1);
    }

    setChecked(newChecked);
    if (setFieldValue !== null && name !== '') setFieldValue(name, newChecked);
  };

  const handleDelete = (id) => {
    const newChecked = [...checked].filter((v) => v !== id);
    setChecked(newChecked);
    if (setFieldValue !== null && name !== '') setFieldValue(name, newChecked);
  };

  const renderList = (list) => {
    let filteredList = list
    if (filter) {
      filteredList = fuse.current.search(filter).map(item => item.item);
    }
    const renderedList = filteredList.map(({ id, description }) => {
      const labelId = `checkbox-list-label-${id}`;

      return (
        <div key={id}>
          <ListItem role={undefined} dense button onClick={handleToggle(id)}>
            <ListItemIcon>
              <Checkbox
                edge="start"
                checked={checked.indexOf(id) !== -1}
                tabIndex={-1}
                disableRipple
                inputProps={{ 'aria-labelledby': labelId }}
              />
            </ListItemIcon>
            <ListItemText id={labelId} primary={description} />
          </ListItem>
          <Divider />
        </div>
      );
    });
    if (renderedList.length > 0) return renderedList;
    else
      return (
        <div className="flex flex-col justify-center items-center h-full opacity-40">
          <img
            className={classes.filterImage}
            src={process.env.PUBLIC_URL + '/img/location-search.svg'}
            alt=""
            loading="lazy"
          />
          <p className={classes.noResultsText}>
            {t('noresults', { search: filter })}
          </p>
        </div>
      );
  };

  return (
    <div className={classes.container}>
      {multiple && (
        <div className={classes.selections}>
          <h3 className="font-semibold">
            {t('createjobpost.steps.locations.selected')}
          </h3>

          {checked.length > 0 ? (
            checked.map((id) => (
              <Chip
                className="m-1"
                label={zones.filter((zone) => zone.id === id)[0].description}
                style={{
                  backgroundColor: themeUtils.colors.lightBlue,
                  fontWeight: 500,
                  color: 'white',
                }}
                icon={<LocationOn className="text-white" />}
                key={id}
                onDelete={() => handleDelete(id)}
              />
            ))
          ) : (
            <div className="flex justify-center items-center font-medium text-sm opacity-50 ">
              {t('createjobpost.steps.locations.notselected')}
            </div>
          )}
        </div>
      )}
      <TextField
        fullWidth
        hiddenLabel
        variant="filled"
        placeholder={t('locationfilter')}
        value={filter}
        onChange={(e) => setFilter(e.target.value)}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <Search />
            </InputAdornment>
          ),
          classes: {
            input: classes.searchInput,
          },
        }}
      />
      <List className={classes.list}>{renderList(zones)}</List>
    </div>
  );
};

export default LocationList;
