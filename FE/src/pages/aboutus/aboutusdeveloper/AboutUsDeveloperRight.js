// css style
import classes from "./AboutUsDeveloperRight.module.css";

const AboutUsDeveloperRight = (props) => {
  return (
    <div className={classes.developerBox}>
      <div className={classes.imgWrapper}>
        <img src={props.img} alt="팀원" />
      </div>
      <div className={classes.developer}>
        <p className={classes.developerName}>{props.name}</p>
        <p className={classes.developerRole}>{props.role}</p>
        <p className={classes.developertext}>{props.text}</p>
      </div>
    </div>
  );
};

export default AboutUsDeveloperRight;
