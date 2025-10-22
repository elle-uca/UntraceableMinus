package org.surino.untraceableminus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.surino.untraceableminus.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {}
