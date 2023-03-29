import { useEffect, useRef } from "react";
import classes from "./Modal.module.css";

const Modal = ({
  setModalOpen,
  content,
  number,
  solutionName,
  solutionSubName,
  hashtag,
  colors,
}) => {
  // 모달 끄기
  const closeModal = () => {
    setModalOpen(false);
  };
  const color1 = colors.color1;
  const color2 = colors.color2;
  const color3 = colors.color3;

  // 모달 외부 클릭시 끄기 처리
  // Modal 창을 useRef로 취득
  const modalRef = useRef(null);

  return (
    <div
      className={classes.modalBackGround}
      onClick={closeModal}
      style={{ backgroundColor: color2 }}
    >
      <div
        className={classes.container}
        ref={modalRef}
        style={{ color: color1 }}
      >
        <div className={classes.containerLeft}>
          <div
            className={classes.vLine}
            style={{ backgroundColor: color1 }}
          ></div>
          <div>
            <div className={classes.TitleBox}>
              <h3 className={classes.number}>{number}</h3>
              <p className={classes.solutionName}>{solutionName}</p>
              <p className={classes.solutionSubName}>{solutionSubName}</p>
              <p className={classes.hashtag}>{hashtag}</p>
            </div>
            <div className={classes.contentBox}>
              <p className={classes.content}>{content}</p>
            </div>
          </div>
        </div>
        <div
          className={classes.containerRight}
          style={{ backgroundColor: color3 }}
        >
          <img src="" alt="이미지" />
          <button
            className={classes.close}
            onClick={closeModal}
            style={{ backgroundColor: color1 }}
          >
            X
          </button>
        </div>
      </div>
    </div>
  );
};

export default Modal;
