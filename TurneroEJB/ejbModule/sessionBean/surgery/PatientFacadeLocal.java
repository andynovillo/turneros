/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.surgery;

import java.util.List;

import javax.ejb.Local;

import entity.surgery.Patient;

/**
 *
 * @author Bryan Valencia
 */
@Local
public interface PatientFacadeLocal {

	void create(Patient patient);

	void edit(Patient patient);

	void remove(Patient patient);

	Patient find(Object id);

	List<Patient> findAll();

	List<Patient> findRange(int[] range);

	int count();

	// Historia clínica es única por unidad médica
	Patient findByClinicHistory(String clinicHistory);

	// Cédula puede repetirse en varios afiliados
	List<Patient> findByCode(String code);

	Patient upsert(Patient patient);

}
