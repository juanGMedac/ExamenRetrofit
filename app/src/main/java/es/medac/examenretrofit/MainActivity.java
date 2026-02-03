package es.medac.examenretrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText etNombre, etNota;
    Button btnEnviar;

    // Variables para la lista
    RecyclerView rvAlumnos;
    AlumnosAdapter adapter;
    List<DatosAlumno> listaAlumnos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Enlazamos la UI
        etNombre = findViewById(R.id.etAlumno);
        etNota = findViewById(R.id.etCalificacion);
        btnEnviar = findViewById(R.id.btnEnviar);
        rvAlumnos = findViewById(R.id.rvAlumnos);

        // 2. Configuramos el RecyclerView
        rvAlumnos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlumnosAdapter(listaAlumnos);
        rvAlumnos.setAdapter(adapter);

        // 3. Cargar datos al abrir la app
        obtenerDatosDelServidor();

        // 4. Lógica del Botón
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recuperamos textos
                String nombre = etNombre.getText().toString();
                String nota = etNota.getText().toString();

                // Validamos que no estén vacíos
                if (!nombre.isEmpty() && !nota.isEmpty()) {
                    // Llamamos al método de enviar
                    enviarDatosAlServidor(nombre, nota);
                } else {
                    Toast.makeText(MainActivity.this, "Faltan datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // MÉTODO PARA ENVIAR (POST)
    private void enviarDatosAlServidor(String nombre, String nota) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://script.google.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServicioWeb servicio = retrofit.create(ServicioWeb.class);
        DatosAlumno alumno = new DatosAlumno(nombre, nota);

        Call<Void> llamada = servicio.enviarDatos(alumno);

        llamada.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "¡Guardado!", Toast.LENGTH_SHORT).show();

                    // Limpiamos los campos
                    etNombre.setText("");
                    etNota.setText("");

                    // 🔥 CLAVE DEL ÉXITO:
                    // Al terminar de guardar, pedimos la lista nueva para que se refresque sola
                    obtenerDatosDelServidor();

                } else {
                    Toast.makeText(MainActivity.this, "Error server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // MÉTODO PARA LEER (GET)
    private void obtenerDatosDelServidor() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://script.google.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServicioWeb servicio = retrofit.create(ServicioWeb.class);

        Call<List<DatosAlumno>> llamada = servicio.obtenerAlumnos();

        llamada.enqueue(new Callback<List<DatosAlumno>>() {
            @Override
            public void onResponse(Call<List<DatosAlumno>> call, Response<List<DatosAlumno>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 1. Limpiamos la lista vieja
                    listaAlumnos.clear();
                    // 2. Añadimos los nuevos datos que vienen de Google
                    listaAlumnos.addAll(response.body());
                    // 3. Avisamos al RecyclerView para que se "pinte" de nuevo
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<DatosAlumno>> call, Throwable t) {
                Log.e("MI_APP", "Error al leer: " + t.getMessage());
            }
        });
    }
}