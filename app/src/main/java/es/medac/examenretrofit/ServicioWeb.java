package es.medac.examenretrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

// Interfaces para Retrofit
public interface ServicioWeb {

    // PEGA AQUÍ LA PARTE LARGA DE TU URL (sin el https://script.google.com/)
    @POST("https://script.google.com/macros/s/AKfycbyI8_sx4bcQ7d4em3L4BXKj5yss1tbN6ZDNtP16JE9IZ_KiJHWBh3hH-3vSyoMr2myX/exec")
    Call<Void> enviarDatos(@Body DatosAlumno datos);

    // El GET para leer. Fíjate que devuelve una List<DatosAlumno>
    @GET("https://script.google.com/macros/s/AKfycbyI8_sx4bcQ7d4em3L4BXKj5yss1tbN6ZDNtP16JE9IZ_KiJHWBh3hH-3vSyoMr2myX/exec")
    Call<List<DatosAlumno>> obtenerAlumnos();
}
