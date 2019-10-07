/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.surgery;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.surgery.Patient;
import sessionBean.AbstractFacade;

/**
 *
 * @author Bryan Valencia
 */
@Stateless
public class PatientFacade extends AbstractFacade<Patient> implements PatientFacadeLocal {
	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public PatientFacade() {
		super(Patient.class);
	}

	@Override
	public Patient findByClinicHistory(String clinicHistory) {

		try {

			return (Patient) entityManager.createNamedQuery("Patient.findByClinicHistory")
					.setParameter("clinicHistory", clinicHistory).getSingleResult();

		} catch (Exception exception) {

			return null;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Patient> findByCode(String code) {

		try {

			return (List<Patient>) entityManager.createNamedQuery("Patient.findByCode").setParameter("code", code)
					.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@Override
	public Patient upsert(Patient patient) {

		Patient temporalPatient = findByClinicHistory(patient.getClinicHistory());

		if (temporalPatient == null) {

			patient.setPatientPk(null);
			entityManager.persist(patient);

		} else {

			patient.setPatientPk(temporalPatient.getPatientPk());
			entityManager.merge(patient);

		}

		return patient;

	}
}
