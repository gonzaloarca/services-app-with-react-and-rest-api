import { themeUtils } from '../theme';

const contractCardStyles = (theme) => ({
  contractCard: {
    boxShadow: themeUtils.shadows.containerShadow,
  },
  contractTitle: {
    fontSize: '1.2rem',
    lineHeight: '1.5rem',
    height: '3rem',
    fontWeight: 700,
    width: '100%',
    WebkitLineClamp: 2,
    display: '-webkit-box',
    WebkitBoxOrient: 'vertical',
    overflowWrap: 'break-word',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
  },
  stateContainer: {
    display: 'flex',
    justifyContent: 'start',
    alignItems: 'center',
    width: '100%',
    color: 'white',
    fontSize: '0.9rem',
    fontWeight: 500,
    padding: '10px 10px',
  },
  avatarSize: {
    height: 40,
    width: 40,
  },
  jobType: {
    color: themeUtils.colors.blue,
    fontSize: '0.9rem',
    fontWeight: 600,
  },
  fieldLabel: {
    color: themeUtils.colors.grey,
    fontSize: '0.8rem',
    fontWeight: 500,
    marginBottom: 5,
  },
  jobImageContainer: {
    position: 'relative',
    marginBottom: 20,
    [theme.breakpoints.up('sm')]: {
      marginBottom: 30,
    },
    display: 'flex',
    justifyContent: 'center',
  },
  scheduledDateContainer: {
    position: 'absolute',
    bottom: -20,
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    fontSize: '0.8rem',
    textAlign: 'center',
    padding: '5px 15px',
    backgroundColor: themeUtils.colors.lightBlue,
    boxShadow: themeUtils.shadows.containerShadow,
    borderRadius: 25,
    color: 'white',
  },
  jobImage: {
    borderRadius: 10,
    height: 170,
    width: 170,
    [theme.breakpoints.down('sm')]: {
      width: '100%',
    },
    objectFit: 'cover',
  },
  scheduledDate: {
    fontSize: '1.3rem',
    fontWeight: 600,
    lineHeight: '1.2rem',
  },
  scheduledTime: {
    fontSize: '1.1rem',
    fontWeight: 600,
    lineHeight: '1.2rem',
  },
  userContainer: {
    display: 'flex',
    alignItems: 'center',
    width: '40%',
  },
  username: {
    width: '80%',
    lineHeight: '1.2rem',
    fontWeight: 500,
    WebkitLineClamp: 2,
    display: '-webkit-box',
    WebkitBoxOrient: 'vertical',
    overflowWrap: 'break-word',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    marginLeft: 10,
  },
  contractActions: {
    display: 'flex',
    width: '100%',
    justifyContent: 'flex-end',
    padding: 7,
  },
});

export default contractCardStyles;
