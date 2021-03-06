/**
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.task;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jbpm.task.utils.CollectionUtils;

@Entity
@Table(name = "JBPM_DEADLINE")
public class Deadline implements Externalizable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "Deadline_Documentation_Id", nullable = true)
  private List<I18NText> documentation = Collections.emptyList();

  @Temporal(TemporalType.DATE)
  @Column(name = "deadline_date")
  private Date date;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "Deadline_Escalation_Id", nullable = true)
  private List<Escalation> escalations = Collections.emptyList();

  private int escalated = 0;

  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeLong(id);

    if (date != null) {
      out.writeBoolean(true);
      out.writeLong(date.getTime());
    } else {
      out.writeBoolean(false);
    }
    CollectionUtils.writeI18NTextList(documentation, out);
    CollectionUtils.writeEscalationList(escalations, out);

    out.writeInt(escalated);
  }

  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    id = in.readLong();

    if (in.readBoolean()) {
      date = new Date(in.readLong());
    }
    documentation = CollectionUtils.readI18NTextList(in);
    escalations = CollectionUtils.readEscalationList(in);

    escalated = in.readInt();
  }

  public long getId() {
    return id;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public List<I18NText> getDocumentation() {
    return documentation;
  }

  public void setDocumentation(final List<I18NText> documentation) {
    this.documentation = documentation;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(final Date date) {
    this.date = date;
  }

  public List<Escalation> getEscalations() {
    return escalations;
  }

  public void setEscalations(final List<Escalation> escalations) {
    this.escalations = escalations;
  }

  public boolean isEscalated() {
    return escalated > 0;
  }

  public void setEscalated(final boolean escalated) {
    this.escalated = escalated ? 1 : 0;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + CollectionUtils.hashCode(documentation);
    result = prime * result + CollectionUtils.hashCode(escalations);
    result = prime * result + escalated;
    result = prime * result + (int) (id ^ (id >>> 32));
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Deadline)) {
      return false;
    }
    final Deadline other = (Deadline) obj;

    if (date == null) {
      if (other.date != null) {
        return false;
      }
    } else if (date.getTime() != other.date.getTime()) {
      return false;
    }

    if (escalated != other.escalated) {
      return false;
    }

    return CollectionUtils.equals(documentation, other.documentation) && CollectionUtils.equals(escalations, other.escalations);
  }
}
