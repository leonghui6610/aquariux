package com.interview.aquariux.trade.service;

import com.interview.aquariux.trade.entities.BaseVO;
import com.interview.aquariux.trade.repo.BaseRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;

@Slf4j
public abstract class BaseServiceSupport<T extends BaseVO, ID extends Serializable, R extends BaseRepo<T, ID>>
        implements BaseService<T, ID, R> {

    @Autowired
    protected R repo;

    @Override
    public T findById(ID id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public T save(T t) {
        try {
            t = repo.save(t);
        } catch (Exception e) {
            log.error("save {},thrown out error {}", t, e.getMessage());
        }
        return t;
    }

    @Override
    public void save(Collection<T> vos) {
        repo.saveAll(vos);
    }

    /*  @Async("delKLinePool")
      @Override
      public void delete(ID id, boolean hard) {
          T t = findById(id);
          log.info("deleting {}, hard {}", t, hard);
          if (!hard && t instanceof AuditVO vo) {
              vo.setDeleted(true);
              t = repo.save(t);
          } else {
              try {
                  repo.delete(t);
              } catch (DataIntegrityViolationException e) {
                  throw new BizException(ResponseCode.DELETE_PARENT);
              }
          }
      }

      // by default hard deletion
      @Override
      public void delete(ID id) {
          delete(id, true);
      }

      @Override
      public void deleteSoftly(ID id) {
          delete(id, false);
      }
  */
    @Override
    @SuppressWarnings("unchecked")
    public T saveRaw(T vo) throws Exception {
        Long id = vo.getId();
        T t;
        if (id == null) {
            t = save(vo);
        } else {
            t = repo.getById((ID) id);
            ValueCopier copier = new ValueCopier();
            try {
                copier.copyProperties(t, vo);
                t = save(t);
            } catch (Exception e) {
                log.error("error when copying properties", e);
                throw new Exception("ERROR SAVE RECORD");
            }
        }
        return t;
    }

    public R getRepo() {
        return repo;
    }

    public void setRepo(R repo) {
        this.repo = repo;
    }

}
