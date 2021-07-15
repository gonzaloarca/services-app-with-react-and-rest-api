import { makeStyles } from '@material-ui/core';
import React from 'react';

const useStyles = makeStyles((theme) => ({
  siteHeader: {
    width: '100%',
    height: 100,
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 20,
    boxShadow: '0 3px 6px rgba(0, 0, 0, 0.16)',
    overflow: 'hidden',
    position: 'relative',
    borderRadius: '5px',
  },
  title: {
    position: 'absolute',
    fontSize: '2rem',
    fontWeight: 'bold',
    color: 'white',
  },
  headerImg: {
    width: '100%',
    height: '11vh',
    objectFit: 'cover',
  }
}));

const SectionHeader = ({
  sectionName,
  imageSrc = "/img/sectionbg.svg",
  filterClass = '',
}) => {
  const classes = useStyles();

  return (
    <div className={classes.siteHeader}>
      <div className={filterClass} />
      <h1 className={classes.title}>{sectionName}</h1>
      <img src={imageSrc} alt="" className={classes.headerImg}/>
    </div>
  );
};

export default SectionHeader;
